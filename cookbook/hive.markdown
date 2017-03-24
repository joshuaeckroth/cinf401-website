---
layout: post
title: Hive
---

# Hive

## Running on delenn

Be sure to change the username.

```
beeline -n jeckroth -p "" -u jdbc:hive2://localhost:10000
```

Initially, you'll have no tables:

```
0: jdbc:hive2://localhost:10000> show tables;
+-----------+--+
| tab_name  |
+-----------+--+
+-----------+--+
No rows selected (2.666 seconds)
0: jdbc:hive2://localhost:10000>
```

{% comment %}

## Queries for StackExchange data

{% highlight sql %}
create external table posts (
  postid string,
  posttype string,
  parentid string,
  dummy string,
  time string,
  body string,
  userid string 
) partitioned by (site string)
row format serde 'org.apache.hadoop.hive.serde2.RegexSerDe'
  with serdeproperties ( 
    "input.regex" = ".*<row.*Id=\"([^\"]+)\".*PostTypeId=\"([^\"]+)\"(?: ParentId=\"([^\"]+)\" |( ))CreationDate=\"([^\"]+)\".*Body=\"([^\"]+)\".*OwnerUserId=\"([^\"]+)\".*"
  )
location '/data/stackexchange-hive/posts';
{% endhighlight %}

{% highlight sql %}
alter table posts 
add partition (site='datascience.stackexchange.com') 
location '/data/stackexchange-hive/posts/datascience.stackexchange.com';

alter table posts
add partition (site='tex.stackexchange.com')
location '/data/stackexchange-hive/posts/tex.stackexchange.com';
{% endhighlight %}

You can also drop partitions:

{% highlight sql %}
alter table posts drop partition (site='datascience.stackexchange.com');
{% endhighlight %}

{% highlight sql %}
select count(*) from posts;
{% endhighlight %}

Returns 173093 for tex, datascience partitions.

{% highlight sql %}
select p1.site, p1.postid, count(*) as answers
from posts p1, posts p2
where p1.postid = p2.parentid
group by p1.site, p1.postid
order by answers;
{% endhighlight %}

Response:

```
tex.stackexchange.com	139873	40
datascience.stackexchange.com	940	45
tex.stackexchange.com	940	45
tex.stackexchange.com	158668	48
tex.stackexchange.com	553	55
tex.stackexchange.com	1319	58
...
```

{% highlight sql %}
select p.site, p.postid, p.userid, panswers.userid, p.time, panswers.time,
  (panswers.t - unix_timestamp(regexp_replace(p.time, 'T', ' '))) as timedelta
from posts as p 
join (
  select unix_timestamp(regexp_replace(time, 'T', ' ')) as t,
    parentid, userid, time from posts)
  as panswers
on panswers.parentid = p.postid
where p.posttype='1';
{% endhighlight %}

Response:

```
datascience.stackexchange.com	997	2972	471	2014-08-19T03:41:24.207	2014-08-20T15:53:29.403	130325
datascience.stackexchange.com	997	2972	2969	2014-08-19T03:41:24.207	2014-08-19T15:21:57.833	42033
datascience.stackexchange.com	997	2972	2452	2014-08-19T03:41:24.207	2014-08-20T03:06:01.753	84277
datascience.stackexchange.com	997	2972	525	2014-08-19T03:41:24.207	2014-08-19T15:47:29.413	43565
tex.stackexchange.com	99724	26419	73	2013-02-24T14:49:40.427	2013-08-08T19:34:04.163	14269464
tex.stackexchange.com	99746	16865	16865	2013-02-24T18:00:52.973	2013-02-24T18:26:44.823	1552
tex.stackexchange.com	99760	26435	5245	2013-02-24T19:48:48.060	2013-02-25T13:38:23.610	64175
tex.stackexchange.com	99762	26436	16267	2013-02-24T20:12:28.873	2013-02-24T20:21:55.730	567
tex.stackexchange.com	99825	26459	1090	2013-02-25T10:23:09.400	2013-02-25T10:29:07.813	358
...
```

Users table:

{% highlight sql %}
create external table users ( 
  userid string, reputation string
) partitioned by (site string)
row format serde 'org.apache.hadoop.hive.contrib.serde2.RegexSerDe' 
with serdeproperties (
  "input.regex" = ".*<row.*Id=\"([^\"]+)\".*Reputation=\"([^\"]+)\".*"
  )
location '/data/stackexchange-hive/users';
{% endhighlight %}

{% highlight sql %}
alter table users add partition (site='datascience.stackexchange.com')
location '/data/stackexchange-hive/users/datascience.stackexchange.com';

alter table users add partition (site='tex.stackexchange.com') 
location '/data/stackexchange-hive/users/tex.stackexchange.com';
{% endhighlight %}

Get user reputations along with their posts:

{% highlight sql %}
select p.site, p.postid, p.userid, panswers.userid, panswers.reputation, p.time, panswers.time,
  (panswers.t - unix_timestamp(regexp_replace(p.time, 'T', ' '))) as timedelta
from posts as p
join (select unix_timestamp(regexp_replace(time, 'T', ' ')) as t, parentid, p2.userid, time, reputation, p2.site
        from posts as p2
        join users as u
        on u.userid = p2.userid and u.site = p2.site
        where posttype='2') as panswers
on panswers.parentid = p.postid and panswers.site = p.site
where p.posttype='1';
{% endhighlight %}


## Queries for Superbowl tweets

{% highlight sql %}
create external table superbowl ( username string, timestamp string, tweet string, retweetcount int, lon float, lat float, country string, name string, address string, type string, placeURL string ) row format delimited fields terminated by '|' location '/data/superbowl';

create table superbowl_uuid as select username, timestamp, tweet, reflect("java.util.UUID", "randomUUID") as uuid from superbowl;

create table superbowl_words as select uuid, trim(w) as word from superbowl_uuid lateral view explode(split(tweet, '[ \?,\!-:]')) superbowl_uuid as w;

# original max() is defined for a column, not two numbers

create temporary macro max2(x INT, y INT) if(x>y,x,y);

create temporary macro tfidf(tf FLOAT, df_t INT, n_docs INT) (log(2, 1+tf) * (log(2, CAST(n_docs as FLOAT)/max2(df_t,1)+1.0)));

# 1536 sec / 48 min across slaves
create table superbowl_word_freqs as select uuid, word, count(word) as freq from superbowl_words where word <> '' group by uuid, word;

# 260 sec / 14 min across slaves
create table superbowl_word_docfreqs as select word, count(uuid) as freq from superbowl_words where word <> '' group by word;

select count(distinct uuid) from superbowl_uuid;
set hivevar:ndocs=8041987; // fill in

# 1811 sec / 52 min across slaves
create table superbowl_tfidf as select tf.uuid, tf.word, tfidf(tf.freq, df.freq, ${ndocs}) as tfidf from superbowl_word_freqs tf join superbowl_word_docfreqs df on (tf.word = df.word);

# 3546 sec / 45 min across slaves
create table superbowl_tfidf_magnitude as select uuid, sqrt(sum(tfidf*tfidf)) as mag from superbowl_tfidf group by uuid;

# 2322 secs, 1 hour across slaves
create table superbowl_tfidf_norm as select a.uuid, a.word, a.tfidf/b.mag as tfidf from superbowl_tfidf a join superbowl_tfidf_magnitude b on a.uuid = b.uuid;

create table superbowl_cosine_sims as select a.uuid, b.uuid, sum(abmult) as sim from superbowl_tfidf_norm as a join superbowl tfidf_norm as b join (select a2.uuid as a_uuid, b2.uuid as b_uuid (a2.tfidf * b2.tfidf) as abmult from superbowl_tfidf_norm as a2 join superbowl_tfidf_norm as b2 on a2.word = b2.word) on a.uuid = a_uuid and b.uuid = b_uuid group by a.uuid, b.uuid;
{% endhighlight %}

{% endcomment %}

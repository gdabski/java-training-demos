-- timezone

SET TIME ZONE '+06:00';
SHOW TIME ZONE;
RESET TIME ZONE;

-- types

SELECT NOW(); -- timestamptz
SELECT current_timestamp; -- timestamptz
SELECT localtimestamp; -- timestamp
SELECT current_date; -- date
SELECT current_time; -- timetz
SELECT localtime; --timetz

SELECT current_timestamp, localtimestamp, current_date, current_time, localtime;

-- szczegółowe konwersje - "with time zone" w nazwie kolumny należy rozumieć jako "with time zone conversion", strefa czasowa nie jest w istocie przechowywana

SELECT val, pg_typeof(val), val::timestamptz, pg_typeof(val::timestamptz) FROM (SELECT TIMESTAMP '2020-12-19 10:23:54' val) sub;
SELECT val, pg_typeof(val), val::timestamp, pg_typeof(val::timestamp) FROM (SELECT TIMESTAMP WITH TIME ZONE '2020-12-19 10:23:54' val) sub;
SELECT val, pg_typeof(val), val::timestamp, pg_typeof(val::timestamp) FROM (SELECT TIMESTAMP WITH TIME ZONE '2020-12-19 10:23:54-06:00' val) sub;
SELECT val, pg_typeof(val), val::timestamp, pg_typeof(val::timestamp) FROM (SELECT TIMESTAMP WITH TIME ZONE '2020-12-19 10:23:54 America/Chicago' val) sub;

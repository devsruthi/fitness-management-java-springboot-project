

--  ---------------------------- CREATE SCHEMA/DATABASE ---------------------------- --
-- ******************************************************************************** --

CREATE SCHEMA health_club_database;

USE health_club_database;

set sql_safe_updates = 0;

Show variables like 'local_infile';
Set global local_infile = 1;
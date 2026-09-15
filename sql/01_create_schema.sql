

--  ---------------------------- CREATE SCHEMA/DATABASE ---------------------------- --
-- ******************************************************************************** --

CREATE SCHEMA health_club_database_system;

USE health_club_database_system;

set sql_safe_updates = 0;

Show variables like 'local_infile';
Set global local_infile = 1;
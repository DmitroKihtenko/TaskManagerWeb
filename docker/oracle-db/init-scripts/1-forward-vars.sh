#!/bin/bash

TABLE_SPACE_HOME=${TABLE_SPACE_HOME:-"/u01/app/oracle/oradata/XE"}
TM_PROFILE=${TM_USER:-task_manager}
TM_USER=${TM_USER:-task_manager}
TM_SCHEMA=${TM_SCHEMA:-$TM_USER}
TM_PASSWORD=${TM_PASSWORD:-task_manager}
TM_SPACE=${TM_SPACE:-"${TM_USER}_space"}
TM_TEMP_SPACE=${TM_TEMP_SPACE:-"${TM_USER}_temp_space"}
SQL_VARS_FILE="/container-entrypoint-initdb.d/init/variables.sql"

forward_variable() {
  local var=$1
  local value=$2
  local sql_vars_file=${3:-$SQL_VARS_FILE}
  echo "define $var=\"$value\"" >> "$sql_vars_file"
}

main() {
  echo -n > $SQL_VARS_FILE
  forward_variable tm_profile "$TM_PROFILE"
  forward_variable user_name "$TM_USER"
  forward_variable schema "$TM_SCHEMA"
  forward_variable user_password "$TM_PASSWORD"
  forward_variable space "$TM_SPACE"
  forward_variable temp_space "$TM_TEMP_SPACE"
  forward_variable space_file "'${TABLE_SPACE_HOME}/${TM_SPACE}.dba'"
  forward_variable temp_space_file "'${TABLE_SPACE_HOME}/${TM_TEMP_SPACE}.dba'"
}

main

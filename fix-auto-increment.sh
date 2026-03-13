#!/bin/bash
# fix-auto-increment.sh
# Ensures all AbbotDB primary key columns have AUTO_INCREMENT enabled
# and sets AUTO_INCREMENT to MAX(pk)+1 to avoid collisions.

HOST="127.0.0.1"
PORT="3306"
DB="AbbotDB"
USER="AbbotUser"
PASS="password"

MYSQL_CMD="mysql -h$HOST -P$PORT -u$USER -p$PASS $DB"

fix_auto_increment() {
    local table=$1
    local pk_col=$2
    local col_def=$3   # e.g. "INT(11) NOT NULL"

    echo -n "  $table.$pk_col ... "

    # Get max PK value + 1 (default to 1 if table is empty)
    local next_val
    next_val=$($MYSQL_CMD -sN -e "SELECT COALESCE(MAX(\`$pk_col\`), 0) + 1 FROM \`$table\`;" 2>&1)
    if [[ $? -ne 0 ]]; then
        echo "ERROR reading max value: $next_val"
        return 1
    fi

    # Modify column to ensure AUTO_INCREMENT is set, and bump the counter
    local result
    result=$($MYSQL_CMD -e \
        "ALTER TABLE \`$table\` MODIFY \`$pk_col\` $col_def AUTO_INCREMENT, AUTO_INCREMENT = $next_val;" 2>&1)
    if [[ $? -ne 0 ]]; then
        echo "ERROR: $result"
        return 1
    fi

    echo "AUTO_INCREMENT set to $next_val"
}

echo "Fixing AUTO_INCREMENT on AbbotDB tables..."
echo ""

fix_auto_increment "BOATS"               "id"                  "INT(11) NOT NULL"
fix_auto_increment "BOAT_CLASS"          "CLASS_ID"            "INT(11) NOT NULL"
fix_auto_increment "BOAT_CLASS_DIVISON"  "ID"                  "INT(11) NOT NULL"
fix_auto_increment "COMPETITION"         "COMP_ID"             "INT(11) NOT NULL"
fix_auto_increment "COURSES"             "id"                  "INT(11) NOT NULL"
fix_auto_increment "FLEET"               "FLEET_ID"            "INT(11) NOT NULL"
fix_auto_increment "FLEET_SELECTOR"      "FLEET_SELECTOR_ID"   "INT(11) NOT NULL"
fix_auto_increment "HANDICAP"            "ID"                  "INT(11) NOT NULL"
fix_auto_increment "HANDICAP_LIMITS"     "HANDICAP_LIMIT_ID"   "INT(11) NOT NULL"
fix_auto_increment "RACE"                "RACE_ID"             "INT(11) NOT NULL"
fix_auto_increment "RACE_RESULT_TBL"     "ID"                  "INT(11) NOT NULL"
fix_auto_increment "RACE_SERIES_TBL"     "ID"                  "INT(11) NOT NULL"
fix_auto_increment "RACE_SERIES_USERS_TBL" "ID"               "INT(11) NOT NULL"
fix_auto_increment "USER_TBL"            "ID"                  "INT(11) NOT NULL"

echo ""
echo "Done."

#!/bin/bash

declare -A dram_configs=( [D1]='d1d8' [D2]='d2' [D4]='d4' [D8]='d1d8' )

_get_dram_config() {
    local select_string=${!dram_configs[@]}" <<"
    PS3="DRAM Options ? # > "
    select j in ${select_string}; do
        case ${j} in
            "<<")
            break
            ;;
            *)
            echo ${dram_configs[${j}]}
            break
            ;;
            esac
    done
}

get_dram_config() {
    DRAM_CONF=${dram_configs[${DRAM:-"empty"}]:-$(_get_dram_config)}
    DRAM_CONF=${DRAM_CONF:-"d4"}
cat << eof
DRAM_CONF = "${DRAM_CONF}"
eof
}

get_dram_config

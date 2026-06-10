#!/bin/bash

declare -A dram_configs=( [D1]='d1' [D2]='d2' [D4]='d4' [D8]='d8' [D1D2]='d1d2' [D1D4]='d1d4' [D1D8]='d1d8' [D2D4]='d2d4' [D2D8]='d2d8' [D4D8]='d4d8' )

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
DRAM_CONF ?= "${DRAM_CONF}"
eof
}

get_dram_config

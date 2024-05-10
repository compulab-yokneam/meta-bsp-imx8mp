SRC_URI:append = " \
        file://common.cfg \
"

# Default is d2d4
SRC_URI:append = " \
        ${@bb.utils.contains('DRAM_CONF', 'd1d8', 'file://d1d8.cfg', 'file://d2d4.cfg', d)} \
"

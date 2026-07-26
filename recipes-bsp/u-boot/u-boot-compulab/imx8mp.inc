SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1d8', 'file://d1d8.cfg', 'file://d2d4.cfg', d)} "

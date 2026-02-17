SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd2', 'file://d2.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd4', 'file://d4.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1d8', 'file://d1d8.cfg', '', d)} "

SRC_URI:append = " \
	file://mp.cfg \
"
SRC_URI:append = "${@bb.utils.contains('DRAM_CONF', 'd4d4', 'file://d4d4.cfg', \
        bb.utils.contains('DRAM_CONF', 'd2d4', 'file://d2d4.cfg', 'file://d1d8.cfg', d), d)}"

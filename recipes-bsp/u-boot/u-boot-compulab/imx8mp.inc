SRC_URI:append = " \
	file://d0.cfg \
	file://spl_size.cfg \
"

SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1', 'file://d1.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd2', 'file://d2.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd4', 'file://d4.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd8', 'file://d8.cfg', '', d)} "

SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1d2', 'file://d1.cfg file://d2.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1d4', 'file://d1.cfg file://d4.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd1d8', 'file://d1.cfg file://d8.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd2d4', 'file://d2.cfg file://d4.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd2d8', 'file://d2.cfg file://d8.cfg', '', d)} "
SRC_URI:append = " ${@bb.utils.contains('DRAM_CONF', 'd4d8', 'file://d4.cfg file://d8.cfg', '', d)} "

SRC_URI:append = " \
	file://0001-compulab-imx8mp-configs-remove-CONFIG_SAMSUNG_4G.patch \
"

SRC_URI:append = " \
        ${@bb.utils.contains('DRAM_CONF', 'd2d4', 'file://d2d4.cfg', 'file://d1d8.cfg', d)} \
"

SRC_URI:append = " \
	file://mp.cfg \
"

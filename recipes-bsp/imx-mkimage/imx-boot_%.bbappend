do_install:append () {
	IMX_BOOT_NAME=$(ls ${D}/boot/ | head -1)
	ln -fs ${IMX_BOOT_NAME} ${D}/boot/imx-boot
}

do_deploy:append () {
	IMX_BOOT_NAME=$(basename $(readlink -e ${DEPLOYDIR}/imx-boot))
	ln -sf ${IMX_BOOT_NAME} ${DEPLOYDIR}/${IMX_BOOT_NAME}_${DRAM_CONF}
}

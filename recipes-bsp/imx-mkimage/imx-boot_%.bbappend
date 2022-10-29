do_install:append () {
        ln -fs ${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/imx-boot
}

dram_conf = "${@bb.utils.contains('DRAM_CONF', 'd2d4', 'd2d4', 'd1d8', d)}"

do_deploy:append () {
	cp ${DEPLOYDIR}/${BOOT_CONFIG_MACHINE}-${IMAGE_IMXBOOT_TARGET} ${DEPLOYDIR}/imx-boot_${MACHINE}_${dram_conf}
}

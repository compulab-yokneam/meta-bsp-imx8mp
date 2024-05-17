do_install:append () {
        ln -fs ${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/imx-boot
}

do_deploy:append () {
	cp ${DEPLOYDIR}/${BOOT_CONFIG_MACHINE}-${IMAGE_IMXBOOT_TARGET} ${DEPLOYDIR}/imx-boot_${MACHINE}_${DRAM_CONF}
}

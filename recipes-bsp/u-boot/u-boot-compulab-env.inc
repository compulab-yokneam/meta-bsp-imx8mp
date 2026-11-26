do_install:append () {

	install -d ${D}${sysconfdir}
	install -m 0644 ${S}/tools/env/fw_env.config ${D}${sysconfdir}/fw_env.config
	install -m 0644 ${KCONFIG_CONFIG_ROOTDIR}/u-boot-initial-env \
		${D}${sysconfdir}/${UBOOT_INITIAL_ENV}
}

UBOOT_INITIAL_ENV = "u-boot-initial-env"
FILES:${PN}-env += "${sysconfdir}/*"
RDEPENDS:${PN}-env += "bash"

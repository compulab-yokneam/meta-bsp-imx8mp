do_install:append () {

	install -d ${D}/etc/
	install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config
}

UBOOT_INITIAL_ENV = "u-boot-initial-env"
FILES:${PN}-env += "/etc/*"
RDEPENDS:${PN}-env += "bash"

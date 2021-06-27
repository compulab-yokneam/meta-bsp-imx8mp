INSTALL_DIR = "${@bb.utils.contains('BBFILE_COLLECTIONS', 'mender', '/opt/compulab/${PN}', '/usr/local/bin', d)}"

SRC_URI_append += "\
	file://cl_setenv \
"

do_install_append () {
	rm -rf ${D}/sbin

	install -d ${D}/etc/
	install -m 0644 ${S}/tools/env/fw_env.config  ${D}/etc/fw_env.config

	install -d ${D}/${INSTALL_DIR}
	install -m 0755 ${S}/tools/env/fw_printenv ${D}/${INSTALL_DIR}/fw_printenv
	install -m 0755 ${S}/tools/env/fw_printenv ${D}/${INSTALL_DIR}/fw_setenv
	install -m 0755 ${WORKDIR}/cl_setenv ${D}/${INSTALL_DIR}/cl_setenv
}

FILES_${PN} = "/etc/* ${INSTALL_DIR}/*"

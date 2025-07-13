DESCRIPTION = "CompuLab U-Boot firmware updater"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=458dc25980a7d4e8e7beb8a4cbb57242"

inherit systemd

RDEPENDS:${PN}:append = " bash compulab-bootloader eeprom-util "

SRC_URI:append = " \
    file://COPYING \
    file://cl-firmware.sh \
    file://cl-firmware.service \
"

FILES:${PN}:append = " \
    ${bindir}/* \
    ${systemd_unitdir}/* \
"

S = "${UNPACKDIR}"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {

    install -d -m 755 ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/cl-firmware.sh ${D}/${bindir}/cl-firmware.sh

    install -d ${D}/${systemd_unitdir}/system
    install -m 644 ${UNPACKDIR}/${BPN}.service ${D}/${systemd_unitdir}/system/

    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
    ln -sf ../${BPN}.service ${D}${systemd_unitdir}/system/multi-user.target.wants/${BPN}.service
}

pkg_postinst_ontarget:${PN} () {
    systemctl --system enable cl-firmware.service
}

pkg_prerm:${PN} () {
    systemctl --system disable cl-firmware.service
}

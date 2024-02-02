FILESEXTRAPATHS:prepend := "${THISDIR}/compulab/${PV}:"

SRC_URI:append = "${@bb.utils.contains('DISTRO_FEATURES', 'linux-rt', 'file://0100-linux-rt-path.patch file://rt.cfg', '', d)}"

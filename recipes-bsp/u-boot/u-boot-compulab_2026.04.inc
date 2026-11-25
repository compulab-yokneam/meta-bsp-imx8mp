LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "u-boot-compulab_v2026.04-imx8mp"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
SRCREV = "${AUTOREV}"

B = "${WORKDIR}/build"

inherit pkgconfig

# Binman and the host tools run using Yocto's native Python environment.
DEPENDS += " \
    efitools-native \
    python3-jsonschema-native \
    python3-pycryptodomex-native \
    python3-pyelftools-native \
    python3-pyyaml-native \
    python3-setuptools-native \
    xxd-native \
"

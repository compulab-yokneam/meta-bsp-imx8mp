LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRCBRANCH = "u-boot-compulab_v2023.04-d1d8_d2d4"
# MCM-iMX8Pv2 support is maintained on the development branch.
SRCBRANCH:mcm-imx8pv2 = "u-boot-compulab_v2023.04-d1d8_d2d4-devel"
SRC_URI = "git://github.com/compulab-yokneam/u-boot-compulab;protocol=https;branch=${SRCBRANCH}"
SRCREV = "${AUTOREV}"

DEPENDS += " python3-setuptools-native "

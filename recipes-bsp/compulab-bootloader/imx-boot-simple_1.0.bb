LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

inherit deploy

ALLOW_EMPTY:${PN} = "1"
PROVIDES += "imx-boot"

DEPENDS += " \
    compulab-bootloader \
"

do_deploy[depends] += " compulab-bootloader:do_deploy " 

do_deploy() {
    if [ -f "${DEPLOY_DIR_IMAGE}/imx-boot.tagged" ];then
        bbplain "${DEPLOY_DIR_IMAGE}/imx-boot.tagged is okay"
    else
        bbwarn "${DEPLOY_DIR_IMAGE}/imx-boot.tagged is missing"
    fi	 
}
addtask deploy

COMPATIBLE_MACHINE = "${MACHINE}-compulab-bootloader"

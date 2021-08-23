SRC_URI_append_ucm-imx8m-plus = " \
	file://0000-ucm-imx8m-plus-Add-support-update-files.patch \
	file://0001-ucm-imx8m-plus-Add-support-Add-files.patch \
	file://0002-ucm-imx8m-plus-Fix-spl-reset.patch \
	file://0003-ucm-imx8m-plus-spl-eeprom-Enable-DM-mode.patch \
	file://0004-ucm-imx8m-plus-dts-Update-u-boot-device-tree.patch \
	file://0005-ucm-imx8m-plus-spl-Update-the-machine-spl.patch \
	file://0006-ucm-imx8m-plus-config-Update-for-SR.patch \
	file://ucm-imx8m-plus_defconfig \
"

SRC_URI_append_imx8mpevk = " \
	file://0100-ucm-imx8m-plus-dram.patch \
"

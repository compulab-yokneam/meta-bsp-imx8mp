SRC_URI_append_mx8mp = " \
	file://0000-ucm-imx8m-plus-Add-support-update-files.patch \
	file://0001-ucm-imx8m-plus-Add-support-Add-files.patch \
	file://0002-ucm-imx8m-plus-Fix-spl-reset.patch \
	file://0003-ucm-imx8m-plus-spl-eeprom-Enable-DM-mode.patch \
	file://0004-ucm-imx8m-plus-dts-Update-u-boot-device-tree.patch \
	file://0005-ucm-imx8m-plus-spl-Update-the-machine-spl.patch \
	file://0006-ucm-imx8m-plus-config-Update-for-SR.patch \
	file://0007-ucm-imx8m-plus-config-Update-ENV-settings.patch \
	file://0008-ucm-imx8m-plus-dts-Add-tty0-as-a-console.patch \
	file://0009-ucm-imx8m-plus-env-Provide-the-CompuLab-bsp_bootcmd.patch \
	file://0010-ucm-imx8m-plus-conf-Update-boot-environment.patch \
	file://0011-ucm-imx8m-plus-conf-Enable-watchdog.patch \
	file://0012-ucm-imx8m-plus-Kconfig-Add-SYS_I2C_EEPROM_BUS-ADDR.patch \
	file://0013-plat-imx8mp-ddr-Remove-the-specific-SoM-limitation.patch \
	file://0014-compulab-imx8mp-Prepare-for-multi-board-support.patch \
	file://0015-compulab-Add-som-imx8m-plus-support.patch \
	file://0016-watchdog-Add-CONFIG_WATCHDOG_AUTOSTART-option.patch \
	file://0017-som-imx8m-plus-confog-Disable-CONFIG_WATCHDOG_AUTOST.patch \
"

SRC_URI_append_ucm-imx8m-plus = " \
	file://ucm-imx8m-plus_defconfig \
"

SRC_URI_append_som-imx8m-plus = " \
	file://som-imx8m-plus_defconfig \
"

SRC_URI_append = " \
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
	file://0017-som-imx8m-plus-config-Disable-CONFIG_WATCHDOG_AUTOST.patch \
	file://0018-compulab-imx8mp-ddr-Fix-service-gpio-error.patch \
	file://0019-som-imx8m-plus-Update-configs.patch \
	file://0020-som-imx8m-plus-Update-dts.patch \
	file://0021-som-imx8m-plus-Init-usdhc1-with-power-down-pin-low.patch \
	file://0022-imx8m-Addresses-the-SDP-common-board_f.c-955-hang-is.patch \
	file://0023-ucm-imx8m-plus-config-Disable-CONFIG_WATCHDOG_AUTOST.patch \
	file://0024-ucm-imx8m-plus-config-Enable-CONFIG_USB_STORAGE.patch \
	file://0025-compulab-imx8m-plus-Move-NET-settings-to-the-machine.patch \
	file://0026-compulab-imx8m-plus-Add-boot_opt.patch \
	file://0027-som-imx8m-plus-Enable-FEC0-enet-clock.patch \
	file://0028-som-imx8m-plus-dts-Add-phy-reset-for-net-ifaces.patch \
	file://0029-som-imx8m-plus-Set-ethprime-to-eth0.patch \
	file://0030-compulab-eeprom-add-support-for-obtaining-2-nd-mac.patch \
	file://0031-imx8m-plus-Add-board-specific-imx_get_mac_from_fuse.patch \
	file://0032-compulab-imx8mp-Add-CONFIG_ATF_LOAD_ADDR-CONFIG_TEE_.patch \
"

SRC_URI_append_ucm-imx8m-plus = " \
	file://ucm-imx8m-plus_defconfig \
"

SRC_URI_append_som-imx8m-plus = " \
	file://som-imx8m-plus_defconfig \
"

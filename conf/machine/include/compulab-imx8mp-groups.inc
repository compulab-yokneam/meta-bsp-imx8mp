SUPER_EVK ??= "0"
SUPER_IOT ??= "0"

MACHINEOVERRIDES .= "${@':compulab-family-groups' if d.getVar('SUPER_EVK') == '1' or d.getVar('SUPER_IOT') == '1' else ''}"
MACHINEOVERRIDES .= "${@':compulab-evk-group' if d.getVar('SUPER_EVK') == '1' else ''}"
MACHINEOVERRIDES .= "${@':compulab-iot-group' if d.getVar('SUPER_IOT') == '1' else ''}"

WKS_FILE:compulab-family-groups = "compulab-imx8mp-groups.wks.in"
WKS_BOOT_SIZE:compulab-family-groups = "256M"

require machine-payload/iot-gate-imx8plus.inc
require machine-payload/iotdin-imx8p.inc
require machine-payload/mcm-imx8m-plus.inc
require machine-payload/som-imx8m-plus.inc
require machine-payload/ucm-imx8m-plus.inc
require machine-payload/ucm-imx8m-plus-sbev.inc

COMPULAB_IMX8MP_EVK_MACHINES = " \
	mcm-imx8m-plus \
	som-imx8m-plus \
	ucm-imx8m-plus \
	ucm-imx8m-plus-sbev \
"

COMPULAB_IMX8MP_IOT_MACHINES = " \
	iot-gate-imx8plus \
	iotdin-imx8p \
"

COMPULAB_IMX8MP_EVK_DEVICETREES = " \
	${COMPULAB_MCM_IMX8M_PLUS_DEVICETREES} \
	${COMPULAB_SOM_IMX8M_PLUS_DEVICETREES} \
	${COMPULAB_UCM_IMX8M_PLUS_DEVICETREES} \
	${COMPULAB_UCM_IMX8M_PLUS_SBEV_DEVICETREES} \
"

COMPULAB_IMX8MP_IOT_DEVICETREES = " \
	${COMPULAB_IOT_GATE_IMX8PLUS_DEVICETREES} \
	${COMPULAB_IOTDIN_IMX8P_DEVICETREES} \
"

COMPULAB_IMX8MP_EVK_CORE_IMAGE_EXTRA_INSTALL = " \
	${COMPULAB_SOM_IMX8M_PLUS_CORE_IMAGE_EXTRA_INSTALL} \
"

COMPULAB_IMX8MP_IOT_IMAGE_INSTALL = " \
	${COMPULAB_IOT_GATE_IMX8PLUS_IMAGE_INSTALL} \
	${COMPULAB_IOTDIN_IMX8P_IMAGE_INSTALL} \
"

COMPULAB_IMX8MP_IOT_CORE_IMAGE_EXTRA_INSTALL = " \
	${COMPULAB_IOT_GATE_IMX8PLUS_CORE_IMAGE_EXTRA_INSTALL} \
	${COMPULAB_IOTDIN_IMX8P_CORE_IMAGE_EXTRA_INSTALL} \
"

COMPULAB_IMX8MP_EVK_MACHINE_FEATURES = " \
	${COMPULAB_MCM_IMX8M_PLUS_MACHINE_FEATURES} \
	${COMPULAB_SOM_IMX8M_PLUS_MACHINE_FEATURES} \
"

COMPULAB_IMX8MP_EVK_MACHINE_FIRMWARE = " \
	${COMPULAB_MCM_IMX8M_PLUS_MACHINE_FIRMWARE} \
"

COMPULAB_IMX8MP_EVK_KERNEL_MODULE_AUTOLOAD = " \
	${COMPULAB_UCM_IMX8M_PLUS_KERNEL_MODULE_AUTOLOAD} \
"

KERNEL_DEVICETREE:append:compulab-evk-group = " \
	${COMPULAB_IMX8MP_EVK_DEVICETREES} \
"

KERNEL_DEVICETREE:append:compulab-iot-group = " \
	${COMPULAB_IMX8MP_IOT_DEVICETREES} \
"

IMAGE_INSTALL:append:compulab-iot-group = " \
	${COMPULAB_IMX8MP_IOT_IMAGE_INSTALL} \
"

CORE_IMAGE_EXTRA_INSTALL:append:compulab-evk-group = " \
	${COMPULAB_IMX8MP_EVK_CORE_IMAGE_EXTRA_INSTALL} \
"

CORE_IMAGE_EXTRA_INSTALL:append:compulab-iot-group = " \
	${COMPULAB_IMX8MP_IOT_CORE_IMAGE_EXTRA_INSTALL} \
"

CORE_IMAGE_EXTRA_INSTALL:append:compulab-family-groups = " \
	${@'u-boot-compulab-multi' if d.getVar('COMPULAB_UBOOT_MULTI_CONFIGS') else ''} \
"

MACHINE_FEATURES:append:compulab-evk-group = " \
	${COMPULAB_IMX8MP_EVK_MACHINE_FEATURES} \
"

MACHINE_FIRMWARE:append:compulab-evk-group = " \
	${COMPULAB_IMX8MP_EVK_MACHINE_FIRMWARE} \
"

KERNEL_MODULE_AUTOLOAD:append:compulab-evk-group = " \
	${COMPULAB_IMX8MP_EVK_KERNEL_MODULE_AUTOLOAD} \
"

COMPULAB_IOT_GATE_IMX8PV2_DEVICETREES = " \
    compulab/iot-gate-imx8pv2.dtb \
    compulab/iot-gate-imx8pv2-headless.dtb \
    compulab/iotg-imx8pv2-can-a.dtbo \
    compulab/iotg-imx8pv2-can-b.dtbo \
    compulab/iotg-imx8pv2-cmd-button.dtbo \
    compulab/iotg-imx8pv2-do0.dtbo \
    compulab/iotg-imx8pv2-do1.dtbo \
    compulab/iotg-imx8pv2-do2.dtbo \
    compulab/iotg-imx8pv2-do3.dtbo \
    compulab/iotg-imx8pv2-hdmi.dtbo \
    compulab/iotg-imx8pv2-modem.dtbo \
    compulab/iotg-imx8pv2-nvme.dtbo \
    compulab/iotg-imx8pv2-rs485-a.dtbo \
    compulab/iotg-imx8pv2-rs485-b.dtbo \
    compulab/iotg-imx8pv2-wifi.dtbo \
    compulab/iotg-imx8pv2-wifi-sdio.dtbo \
"

COMPULAB_IOT_GATE_IMX8PV2_IMAGE_INSTALL = " \
    iot-gate-imx8pv2-config \
"

COMPULAB_IOT_GATE_IMX8PV2_CORE_IMAGE_EXTRA_INSTALL = " \
    gpsd gps-utils ppp modemmanager \
"

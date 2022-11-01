# Building Boot Firmware for CompuLab's i.MX8M Plus products

* Define the following environment variable:

|Description|Command Line|
|---|---|
|CompuLab branch name|export CPL_BRANCH=kirkstone|

* Create a folder to organize the files:
<pre>
mkdir imx8mp
cd imx8mp
</pre>

* Download CompuLab BSP
<pre>
git clone -b ${CPL_BRANCH} https://github.com/compulab-yokneam/meta-bsp-imx8mp.git
export DEV_SHELL=$(pwd)/meta-bsp-imx8mp/Documentation
</pre>

* Run the developmet shell:

<pre>
cd ${DEV_SHELL}
./cl-buildenvironment.shell
</pre>

The `flash.bin` image of the ucm machine can be created with UART1 or UART2 debug console.<br>
The default is UART2. In case of using a non-default port, issue this command:
<pre>
cd ${DEV_SHELL}
CPL_DEBUG_UART=1 ./cl-buildenvironment.shell
</pre>

* Follow the on screen instructions:
<pre>
1) ucm-imx8m-plus
2) som-imx8m-plus
3) iot-gate-imx8plus
4) <<
Select a machine: ? 1

Available functions:
[0] - get_compatible_machine
[1] - get_dram_config
[2] - get_compulab_bootloader
[3] - clean_all
[4] - exit

cl-buildenvironment [ MACHINE=ucm-imx8m-plus; DEBUG_UART=2; DRAM=D1D8; CROSS_COMPILE=/home/user/Applications/gcc-arm-10.3-2021.07-x86_64-aarch64-none-linux-gnu/bin/aarch64-none-linux-gnu- ]

Bootloader build status: ucm-imx8m-plus-flash.bin[-];  > 2

Bootloader build status: ucm-imx8m-plus-flash.bin[x];  > ls -ltr ../results | tail -3

-rw-rw-r--  1 user user 1840680 Nov  1 09:47 ucm-imx8m-plus-flash.bin-d1d8
lrwxrwxrwx  1 user user      29 Nov  1 09:47 ucm-imx8m-plus-flash.bin -> ucm-imx8m-plus-flash.bin-d1d8
-rw-rw-r--  1 user user     878 Nov  1 09:47 flash.log

Available functions:
[0] - get_compatible_machine
[1] - get_dram_config
[2] - get_compulab_bootloader
[3] - clean_all
[4] - exit

cl-buildenvironment [ MACHINE=ucm-imx8m-plus; DEBUG_UART=2; DRAM=D1D8; CROSS_COMPILE=/home/user/Applications/gcc-arm-10.3-2021.07-x86_64-aarch64-none-linux-gnu/bin/aarch64-none-linux-gnu- ]

Bootloader build status: ucm-imx8m-plus-flash.bin[x];  > 1
1) D1D8
2) D2D4
Select a dram config: ? 2

Available functions:
[0] - get_compatible_machine
[1] - get_dram_config
[2] - get_compulab_bootloader
[3] - clean_all
[4] - exit

cl-buildenvironment [ MACHINE=ucm-imx8m-plus; DEBUG_UART=2; DRAM=D2D4; CROSS_COMPILE=/home/user/Applications/gcc-arm-10.3-2021.07-x86_64-aarch64-none-linux-gnu/bin/aarch64-none-linux-gnu- ]

Bootloader build status: ucm-imx8m-plus-flash.bin[x];  > 2

Bootloader build status: ucm-imx8m-plus-flash.bin[x];  > ls -ltr ../results/ | tail -3
-rw-rw-r--  1 user user     878 Nov  1 09:53 flash.log
-rw-rw-r--  1 user user 1840680 Nov  1 09:53 ucm-imx8m-plus-flash.bin-d2d4
lrwxrwxrwx  1 user user      29 Nov  1 09:53 ucm-imx8m-plus-flash.bin -> ucm-imx8m-plus-flash.bin-d2d4
</pre>

* Precompiled `flash.bin` images can be downloaded from [here](https://drive.google.com/drive/folders/1e4JRoArD_yecUv4ppIy5--FK72Ofcwi_)

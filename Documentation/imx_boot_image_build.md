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

* Run the developmet shell and follow the on screen instructions:

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

* Precompiled `flash.bin` images can be downloaded from [here](https://drive.google.com/drive/folders/1e4JRoArD_yecUv4ppIy5--FK72Ofcwi_)

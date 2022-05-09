# Building Boot Firmware for CompuLab's i.MX8M Plus products

* Define the following environment variable:

|Description|Command Line|
|---|---|
|CompuLab branch name|export CPL_BRANCH=ucm-imx8m-plus-r1.1|

* Create a folder to organize the files:
<pre>
mkdir -p imx8mp/devel
cd imx8mp
</pre>

* Download CompuLab BSP
<pre>
git clone -b ${CPL_BRANCH} https://github.com/compulab-yokneam/meta-bsp-imx8mp.git
export DEV_SHELL=$(pwd)/meta-bsp-imx8mp/Documentation
</pre>

* Run the developmet shell and follow the on screen instructions:
<pre>
cd devel
${DEV_SHELL}/cl-buildenvironment.shell
</pre>

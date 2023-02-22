# Building Linux Kernel for CompuLab's i.MX8M Plus products

* Define the following environment variable:

|Description|Command Line|
|---|---|
|CompuLab branch name|export CPL_BRANCH=ucm-imx8m-plus-r2.0|

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
KERNEL=1 ./cl-buildenvironment.shell
</pre>

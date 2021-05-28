# Linaro toolchain how to

* Downlaod the Linaro compiler:
<pre>
cd ~/Downloads
wget https://armkeil.blob.core.windows.net/developer/Files/downloads/gnu-a/9.2-2019.12/binrel/gcc-arm-9.2-2019.12-x86_64-aarch64-none-linux-gnu.tar.xz
</pre>

* Install it:
<pre>
sudo tar -C /opt -xf ~/Downloads/gcc-arm-9.2-2019.12-x86_64-aarch64-none-linux-gnu.tar.xz
</pre>

* Set environment variables:
<pre>
export ARCH=arm64
export CROSS_COMPILE=/opt/gcc-arm-9.2-2019.12-x86_64-aarch64-none-linux-gnu/bin/aarch64-none-linux-gnu-
</pre>

* Get the compiler version information.<br>Make sure that the compiler installed and the environment set correctly, issue:
<pre>
${CROSS_COMPILE}gcc --version
aarch64-none-linux-gnu-gcc (GNU Toolchain for the A-profile Architecture 9.2-2019.12 (arm-9.10)) 9.2.1 20191025
Copyright (C) 2019 Free Software Foundation, Inc.
This is free software; see the source for copying conditions.  There is NO
warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
</pre>

# Optional ISP support

This directory contains optional camera and ISP integrations.

To exclude all integrations in this directory from a build, add the
following setting to `conf/local.conf`:

```bitbake
BBMASK += "meta-bsp-imx8mp/recipes-isp/"
```

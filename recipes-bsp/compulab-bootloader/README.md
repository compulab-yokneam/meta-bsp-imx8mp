# compulab-bootloader

## Make it the preferred bootloader provider:
* conf/machine/compulab-imx8mp.inc
```
PREFERRED_PROVIDER_virtual/bootloader:compulab-mx8mp = "compulab-bootloader"
```

## Update the core image install list:
* conf/local.conf
```
CORE_IMAGE_EXTRA_INSTALL += " compulab-bootloader compulab-bootloader-env "
```

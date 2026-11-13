# Adding a machine to meta-bsp-imx8mp

This cookbook adds a CompuLab i.MX8MP board to this layer. It uses
`my-board` as the machine name and `COMPULAB_MY_BOARD` as the payload-variable
prefix. Replace these throughout with your board's names.

Paths below are relative to the `meta-bsp-imx8mp` layer unless a different
repository is named. Run environment-setup commands from the BSP workspace
root, where `compulab-setup-env` and `sources/` are located.

## 1. Decide what the machine needs

Before editing the layer, identify:

- The Linux DTB to boot by default and any additional DTBs or overlays.
- The U-Boot defconfig, U-Boot DTB, and Linux `fdtfile` selected by U-Boot.
- The supported DRAM configurations: split `d1d8`/`d2d4`, or one image that
  supports all capacities.
- Required firmware, userspace packages, machine features, and modules that
  need explicit loading.
- Whether other machines should receive this board's payload through
  `SUPER_EVK` or `SUPER_IOT`.
- Whether to include the board in either optional family bootloader build.

Linux and U-Boot board support must already exist or be added in their own
repositories. A Yocto machine file selects that support; it does not create
board drivers, pinmux definitions, DRAM timings, or boot firmware.

For a different SoC, first provide an appropriate common machine include.
The existing `compulab-imx8mp.inc` contains i.MX8MP-specific firmware,
addresses, tuning, and provider selections.

## 2. Use this file checklist

| File | Action | When |
|---|---|---|
| `conf/machine/my-board.conf` | Create | Always |
| `conf/machine/include/machine-payload/my-board.inc` | Create | Always, following this layer's shared-payload convention |
| [conf/machine/include/compulab-imx8mp-groups.inc](../conf/machine/include/compulab-imx8mp-groups.inc) | Add the payload `require`; optionally extend group inventories | Always for registration; group membership is optional |
| [README.md](../README.md) | Add the supported product and machine-selection command | Always |
| `Documentation/my-board.md` | Create a board guide | When setup, interfaces, or limitations need explanation |
| `recipes-utils/my-board-config/my-board-config_1.0.bb` | Create a package recipe | Only for a new board configuration application |
| `recipes-bsp/my-board-firmware/my-board-firmware_1.0.bb` | Create a firmware recipe | Only if existing firmware packages do not cover the board |
| `tools/my-board/*.sh` | Create setup hooks | Only for board-specific initialization of a new build directory |
| [recipes-bsp/u-boot/u-boot-compulab_2023.04.inc](../recipes-bsp/u-boot/u-boot-compulab_2023.04.inc) | Add a machine-specific source branch override | Only if the normal branch lacks the board |
| [recipes-bsp/u-boot/u-boot-compulab/imx8mp.inc](../recipes-bsp/u-boot/u-boot-compulab/imx8mp.inc) | Override the DRAM fragments | Only for a different DRAM-selection scheme |
| `templates/multiconfig/my-board-d1d8.conf` and `my-board-d2d4.conf` | Create | To join the family multiconfig build |
| [conf/u-boot-family-multiconfig.conf](../conf/u-boot-family-multiconfig.conf) | Extend both configuration lists | To join the family multiconfig build |
| [recipes-bsp/compulab-bootloader/README.md](../recipes-bsp/compulab-bootloader/README.md) | Update the supported bundle matrix | If single-recipe bundle membership changes |
| [Documentation/imx_boot_image_build.md](imx_boot_image_build.md) and [advanced-features.md](advanced-features.md) | Update machine lists and container counts | If the documented build matrix changes |

Normally, do not create another Linux or U-Boot recipe for each board.
Requiring the common machine include supplies the `compulab-mx8mp` override,
which makes the existing recipes compatible with the new machine.

## 3. Create the payload inventory

Create `conf/machine/include/machine-payload/my-board.inc`:

```bitbake
COMPULAB_MY_BOARD_DEVICETREES = " \
    compulab/my-board.dtb \
"

COMPULAB_MY_BOARD_IMAGE_INSTALL = ""
COMPULAB_MY_BOARD_CORE_IMAGE_EXTRA_INSTALL = ""
COMPULAB_MY_BOARD_MACHINE_FEATURES = ""
COMPULAB_MY_BOARD_MACHINE_FIRMWARE = ""
COMPULAB_MY_BOARD_KERNEL_MODULE_AUTOLOAD = ""
```

Populate only the lists the board needs. Empty lists are safe placeholders;
you can omit them and their references if you do not need them.

| Payload suffix | Contents |
|---|---|
| `DEVICETREES` | Built `.dtb` and `.dtbo` paths, with the default Linux DTB first |
| `IMAGE_INSTALL` | Board configuration applications and other image packages |
| `CORE_IMAGE_EXTRA_INSTALL` | Additional tools for core-image-based images, such as GPS or modem utilities |
| `MACHINE_FEATURES` | Additional hardware feature tokens used by recipes and package groups |
| `MACHINE_FIRMWARE` | Firmware **package names**, not firmware filenames |
| `KERNEL_MODULE_AUTOLOAD` | Module names that need loading through generated modules-load configuration |

Add overlays only when they exist and are built by the kernel Makefile.
List the output name, such as `compulab/my-board-can.dtbo`, rather than the
source `.dtso`. Keep DTB paths consistent with the existing inventories.

Every package named here needs a recipe or another package provider in the
enabled layers. Do not add a placeholder `my-board-config` package until it
can actually be built.

For a new application or firmware package, add its recipe under the matching
`recipes-*` directory and any local inputs in its recipe's `files/`
directory. Supply its license metadata, install rules, and runtime
dependencies. The layer's existing `BBFILES` pattern discovers recipes at
`recipes-*/*/*.bb`; adding a machine normally needs no `conf/layer.conf`
change. If the provider belongs to another layer, ensure that layer is
included in the BSP's manifests and build configuration.

## 4. Register the payload include

In `conf/machine/include/compulab-imx8mp-groups.inc`, add this beside the
existing payload includes:

```bitbake
require machine-payload/my-board.inc
```

This registration is needed even if the board will not join a grouped image.
The machine includes `compulab-imx8mp.inc`, which includes the groups file;
that chain loads the payload variables used by the machine.

Do not also require the same payload directly from the machine file when
using this registration convention.

## 5. Create the machine configuration

Create `conf/machine/my-board.conf`:

```bitbake
#@TYPE: Machine
#@NAME: CompuLab My Board
#@SOC: i.MX8MP
#@DESCRIPTION: Machine configuration for CompuLab My Board
#@MAINTAINER: compulab@compulab.co.il

require compulab-imx8mp.inc

KERNEL_DEVICETREE = "${COMPULAB_MY_BOARD_DEVICETREES}"
UBOOT_DTB_NAME = "my-board.dtb"

IMAGE_INSTALL:append = " ${COMPULAB_MY_BOARD_IMAGE_INSTALL} "
CORE_IMAGE_EXTRA_INSTALL += " ${COMPULAB_MY_BOARD_CORE_IMAGE_EXTRA_INSTALL} "
MACHINE_FEATURES += " ${COMPULAB_MY_BOARD_MACHINE_FEATURES} "
MACHINE_FIRMWARE:append = " ${COMPULAB_MY_BOARD_MACHINE_FIRMWARE} "
KERNEL_MODULE_AUTOLOAD += " ${COMPULAB_MY_BOARD_KERNEL_MODULE_AUTOLOAD} "
```

The common include already selects:

- Linux provider `linux-compulab` and `compulab_v8_defconfig`.
- U-Boot provider `u-boot-compulab` and `${MACHINE}_defconfig`.
- Default `DRAM_CONF = "d2d4"` through a weak default.
- Common i.MX8MP tuning, firmware, console, and machine features.

Thus `MACHINE=my-board` normally selects `my-board_defconfig` automatically.
If the board deliberately shares another defconfig, set
`UBOOT_CONFIG_BASENAME = "existing-board"` in its machine file.

`UBOOT_DTB_NAME` refers to the U-Boot device tree. Check the U-Boot board
configuration or environment separately to ensure its Linux `fdtfile` loads
the intended first Linux DTB; setting this variable alone is not a Linux
boot-environment change.

Keep board-specific settings in the machine or payload include. Edit
`compulab-imx8mp.inc` only for changes intended for the entire family.

## 6. Check the Linux and U-Boot sources

In the Linux repository, add or verify:

- `arch/arm64/boot/dts/compulab/my-board.dts` and any shared `.dtsi` files.
- Required `.dtso` overlays and their build entries in the appropriate
  CompuLab device-tree Makefile.
- The drivers needed by the board in the shared configuration or supported
  configuration fragments.

In the U-Boot repository, add or verify:

- `configs/my-board_defconfig` and its board/Kconfig integration.
- The U-Boot device tree, DRAM setup, and boot environment.
- The DRAM fragments consumed by the recipe.

The layer's source branches are selected in
[linux-compulab_6.18.20.inc](../recipes-kernel/linux/linux-compulab_6.18.20.inc)
and
[u-boot-compulab_2023.04.inc](../recipes-bsp/u-boot/u-boot-compulab_2023.04.inc).
If U-Boot support is on a different branch, add a scoped override there:

```bitbake
SRCBRANCH:my-board = "your-board-support-branch"
```

Use a branch that actually exists and contains the required board support.
Publish the source commits, or use a configured `devtool` workspace during
local development. An unpushed workspace change is not automatically
available to a fresh BSP checkout.

For ordinary split-DRAM boards, the U-Boot recipe already merges
`d0.config ${DRAM_CONF}.config`. For an all-capacity image, follow
[mcm-imx8pv2.conf](../conf/machine/mcm-imx8pv2.conf) and its fragment override
in
[imx8mp.inc](../recipes-bsp/u-boot/u-boot-compulab/imx8mp.inc):
set the appropriate DRAM label, disable split-DRAM multiconfig handling, and
select the real fragments. Do not assume that an all-capacity label names a
single existing `.config` file.

## 7. Optionally add grouped-image support

Group support lets another machine's image carry this board's DTBs,
firmware, and tools. Edit `conf/machine/include/compulab-imx8mp-groups.inc`.
Choose `EVK` or `IOT` according to the intended payload family.

For an IOT board, append the new payload to the existing inventories:
place these additions after the corresponding aggregate definitions, or
insert the new references directly into their existing lists.

```bitbake
COMPULAB_IMX8MP_IOT_DEVICETREES += "${COMPULAB_MY_BOARD_DEVICETREES}"
COMPULAB_IMX8MP_IOT_IMAGE_INSTALL += "${COMPULAB_MY_BOARD_IMAGE_INSTALL}"
COMPULAB_IMX8MP_IOT_CORE_IMAGE_EXTRA_INSTALL += "${COMPULAB_MY_BOARD_CORE_IMAGE_EXTRA_INSTALL}"
```

For firmware, features, and autoload settings, check that both pieces exist:
an aggregate variable containing the board's list, and an override that
applies that aggregate when the group is enabled. For example, if the IOT
firmware aggregate is not already present, add:

```bitbake
COMPULAB_IMX8MP_IOT_MACHINE_FIRMWARE = "${COMPULAB_MY_BOARD_MACHINE_FIRMWARE}"

MACHINE_FIRMWARE:append:compulab-iot-group = " \
    ${COMPULAB_IMX8MP_IOT_MACHINE_FIRMWARE} \
"
```

If it already exists, extend it instead of replacing its other members.
Follow the same pattern for `MACHINE_FEATURES` and
`KERNEL_MODULE_AUTOLOAD` when needed. The EVK group already demonstrates
these hooks; the groups do not necessarily have identical aggregate lists.

Adding only DTBs leaves the group without the board-specific firmware or
userspace dependencies. Conversely, enabling a group adds payloads; it does
not change the selected machine's U-Boot configuration or default Linux DTB.

## 8. Optionally extend the bootloader builds

There are two separate family-build mechanisms. Neither is required to
build an ordinary image for the new machine.

### Single-recipe bundle: compulab-bootloader

`COMPULAB_IMX8MP_EVK_MACHINES` and `COMPULAB_IMX8MP_IOT_MACHINES` in the
groups include feed the optional `compulab-bootloader` recipe. They are
**bootloader-bundle lists**, not the switch that adds DTBs to grouped images.

Add `my-board` to the appropriate list only when the bundle's source branch
contains `my-board_defconfig` and the board supports its `d1d8`/`d2d4`
fragment workflow. The recipe builds each listed machine for both subsets
by default. Update the documented matrix and container counts as well.

A machine-specific source-branch or fragment override for
`u-boot-compulab` does not automatically affect the separate
`compulab-bootloader` recipe. An all-capacity development board may join an
image payload group while remaining outside this bootloader bundle.

See the [bundle documentation](../recipes-bsp/compulab-bootloader/README.md).
Adding a container to the bundle is also separate from teaching any firmware
updater to map a new EEPROM product identity to that container.

### Multiconfig bundle: u-boot-compulab-multi

Create `templates/multiconfig/my-board-d1d8.conf`:

```bitbake
MACHINE = "my-board"
DRAM_CONF = "d1d8"
COMPULAB_DRAM_MULTICONFIG = "0"
TMPDIR = "${TOPDIR}/tmp-${BB_CURRENT_MC}"
```

Create `templates/multiconfig/my-board-d2d4.conf` with the same content except
`DRAM_CONF = "d2d4"`.

In `conf/u-boot-family-multiconfig.conf`, extend **both** lists:

```bitbake
COMPULAB_UBOOT_MULTI_CONFIGS += "my-board-d1d8 my-board-d2d4"

COMPULAB_UBOOT_MULTI_MATRIX += " \
    my-board-d1d8:my-board:d1d8 \
    my-board-d2d4:my-board:d2d4 \
"
```

Each matrix entry is `multiconfig:machine:DRAM_CONF`. The recipe rejects
mismatches between these lists. Keep per-configuration `TMPDIR` isolation.

Re-enter the environment through `compulab-setup-env` to copy new templates
into `${BUILDDIR}/conf/multiconfig`. The helper copies missing files only;
if you change an existing template, update its build-directory copy too.
Do not enable both `dram-multiconfig.conf` and
`u-boot-family-multiconfig.conf` in the same build.

## 9. Document and validate the machine

Add the product and `export MACHINE=my-board` row to the main README.
Create a board guide when necessary, and link it from the README's final
Additional documentation chapter. Record the default DTB, DRAM selection,
available overlays, and any interfaces still awaiting validation.

If the board needs setup-time customization, `compulab-setup-env` sources
files under `tools/${MACHINE}/` when creating a new build directory. These
hooks do not run when re-entering an existing directory; keep normal machine
settings in the machine configuration so they do not depend on a setup hook.

Start with a dedicated build directory:

```sh
export MACHINE=my-board
source compulab-setup-env build-${MACHINE}
bitbake -p
bitbake -e virtual/kernel > kernel.env
bitbake -e u-boot-compulab > u-boot.env
bitbake -e imx-image-full > image.env
```

For an existing directory, check `conf/local.conf` for a fixed `MACHINE`
assignment that would override the exported value.

Inspect the expanded values, not just the source text:

| Environment | Check |
|---|---|
| `kernel.env` | `MACHINE`, `KBUILD_DEFCONFIG`, `KERNEL_DEVICETREE`, and the source branch |
| `u-boot.env` | `UBOOT_MACHINE`, `UBOOT_DTB_NAME`, `DRAM_CONF`, `COMPULAB_UBOOT_CONFIG_FRAGMENTS`, and the source branch |
| `image.env` | `IMAGE_INSTALL`, `MACHINE_FIRMWARE`, `MACHINE_FEATURES`, and `KERNEL_MODULE_AUTOLOAD` |

Resolve dependencies and then build:

```sh
bitbake -n core-image-full-cmdline
bitbake u-boot-compulab virtual/kernel
bitbake imx-image-full
```

Check `${BUILDDIR}/tmp/deploy/images/${MACHINE}/` for the boot container,
selected DTBs/overlays, and image. Metadata parsing and dry runs do not
replace compilation or a boot test.

If grouped images are supported, also test another machine with the new
board's group enabled, and test the new machine with both groups enabled.
Verify that its payload and firmware are present and the first Linux DTB
and selected bootloader remain correct.

If family bootloader support was added, enable and build each relevant
mechanism separately, following [Advanced Features](advanced-features.md).
Check both DRAM variants rather than assuming that one successful build
covers the other.

Before committing, run `git diff --check`, inspect the staged file list,
and boot the resulting image on the board. Confirm the intended DRAM size,
console, storage, network interfaces, and any optional overlays you claim
to support. Record build-only results separately from hardware validation.

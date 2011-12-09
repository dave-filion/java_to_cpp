extern struct { int mode; } *plat;
extern int  dev_err(), dev, EINVAL;

int main() {
	switch (plat->mode) {
	case 1:
#ifdef CONFIG_USB_MUSB_HDRC_HCD
		break;
#else
		goto bad_config;
#endif
	case 2:
#ifdef CONFIG_USB_GADGET_MUSB_HDRC
		break;
#else
		goto bad_config;
#endif
	case 3:
#ifdef CONFIG_USB_MUSB_OTG
		break;
#else
bad_config:
#endif
	default:
		dev_err(dev, "incompatible Kconfig role setting\n");
		return -EINVAL;
	}
}

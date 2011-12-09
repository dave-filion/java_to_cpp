int main() {
#ifdef CONFIG_SND_AC97_POWER_SAVE
	/* do cold reset - the full ac97 powerdown may leave the controller
	 * in a warm state but actually it cannot communicate with the codec.
	 */
	iputdword(chip, ICHREG(GLOB_CNT), cnt & ~ICH_AC97COLD);
	cnt = igetdword(chip, ICHREG(GLOB_CNT));
	udelay(10);
	iputdword(chip, ICHREG(GLOB_CNT), cnt | ICH_AC97COLD);
	msleep(1);
#else
	/* finish cold or do warm reset */
	cnt |= (cnt & ICH_AC97COLD) == 0 ? ICH_AC97COLD : ICH_AC97WARM;
	iputdword(chip, ICHREG(GLOB_CNT), cnt);
	end_time = (jiffies + (HZ / 4)) + 1;
	do {
		if ((igetdword(chip, ICHREG(GLOB_CNT)) & ICH_AC97WARM) == 0)
			goto __ok;
		schedule_timeout_uninterruptible(1);
	} while (time_after_eq(end_time, jiffies));
	snd_printk(KERN_ERR "AC'97 warm reset still in progress? [0x%x]\n",
		   igetdword(chip, ICHREG(GLOB_CNT)));
	return -EIO;

      __ok:
#endif
after;
}

SDK := /opt/android-sdk/
TOOLS := $(SDK)/tools
PLATFORM_TOOLS := $(SDK)/platform-tools

TARGETS := package build clean uninstall install uninstall-emulator install-emulator run
ADB := $(PLATFORM_TOOLS)/adb
ANT := /usr/share/java/apache-ant/bin/ant
EMULATOR := $(TOOLS)/emulator
ANDROID := $(TOOLS)/android


PACKAGE := org.foss.defaultpasswords

# Assigned by user
AVD := '2.3.3-default'
# other options include release
MODE := debug


APK := 'DefaultPasswords-$(MODE).apk'



build:
	$(ANDROID) update project --path .
	$(ANT) $(MODE)

uninstall:
	$(ADB) uninstall $(PACKAGE)

install: uninstall build
	$(ADB) install bin/$(APK)

install-emulator:
	$(ADB) -e install bin/$(APK)
uninstall-emulator:
	$(ADB) uninstall -e $(APK)

run:
	$(EMULATOR) -avd $(AVD)
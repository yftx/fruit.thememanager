LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_PACKAGE_NAME := ThemeManager

# don't apply dalvik preoptimization to ease development
#LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)
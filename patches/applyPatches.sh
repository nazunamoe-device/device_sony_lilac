#!/bin/bash

cd .. && cd .. && cd .. && cd ..
cp device/sony/lilac/patches/assistant.patch frameworks/base
cd frameworks/base

# Enable lockscreen assistant
git revert 7d7bebd4bd58149b10e3b44fb9c559d25762d9fd

# Apply assistant uid patch
git apply assistant.patch

cd .. && cd ..
cd packages/apps/Settings

# Enable GooglePlaySystemUpdate
git revert fcca4fa9da67d3409df9729dde0c14c60f831af5

cd .. && cd .. && cd ..
cp device/sony/lilac/patches/mem.patch hardware/qcom-caf/msm8998/display
cd hardware/qcom-caf/msm8998/display

# Fix camera mem leak
git apply mem.patch

cd .. && cd .. && cd .. && cd ..
echo "Done."

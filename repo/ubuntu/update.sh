#!/bin/bash

#required packages : dpkg-dev

rm *.gz 2>/dev/null
rm InRelease 2>/dev/null
rm Release 2>/dev/null
rm Release.gpg 2>/dev/null

dpkg-scanpackages . | gzip -c > Packages.gz

apt-ftparchive release . > TopRelease
mv TopRelease Release

gpg --clearsign -o InRelease Release
gpg -abs -o Release.gpg Release

gzip InRelease
gzip Release
gzip Release.gpg

if [ ! -f javaforce.gpg ]; then
  cp ~/.gnupg/pubring.gpg ./javaforce.gpg
fi
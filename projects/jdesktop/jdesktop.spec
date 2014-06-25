Buildroot: /.
Name: jdesktop
Version: 0.22
Release: 1
Summary: Java Desktop Environment
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, jfile, jhelp, openbox, acpi, pulseaudio, pulseaudio-utils, gnome-backgrounds
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%post
#! /bin/sh

set -e

if [ $1 = "1" ]; then
  update-alternatives --install /usr/bin/x-session-manager x-session-manager /usr/bin/jdesktop 90
fi

%pre
#!/bin/sh

set -e

mkdir -p /etc/jdesktop

%preun
#!/bin/sh

set -e

%postun
#!/bin/sh -e

if [ $1 = "0" ]; then
  update-alternatives --remove x-window-manager /usr/bin/jdesktop
fi

%description

Java Startup and Logon Process

%files

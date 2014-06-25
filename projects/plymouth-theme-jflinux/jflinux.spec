Buildroot: /.
Name: plymouth-theme-jflinux
Version: 0.2
Release: 1
Summary: Plymouth bootup logo
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: plymouth-plugin-script
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Plymouth bootup logo

%post

plymouth-set-default-theme jflinux
mkinitrd /boot/initrd.img-`uname -r`

%files

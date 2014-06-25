Buildroot: /.
Name: jinstall
Version: 0.4
Release: 1
Summary: Linux OS installer
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, jparted, rsync, grub2, grub2-tools
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Linux OS installer

%files

Buildroot: /.
Name: jrecorddesktop
Version: 0.3
Release: 1
Summary: Records Desktop
License: LGPL
Distribution: Fedora
Group: Applications/System
Requires: javaforce, ffmpeg
BuildArch: noarch

%define _rpmdir ../
%define _rpmfilename %%{NAME}-%%{VERSION}-%%{RELEASE}.noarch.rpm
%define _unpackaged_files_terminate_build 0

%description

Records Desktop to Video file

%files

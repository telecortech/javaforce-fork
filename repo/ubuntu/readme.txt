This is the JavaForce DEB Repository.

You will need to install dpkg-dev package:
  sudo apt-get install dpkg-dev

Run gpg to create the key to sign the packages:
  gpg --gen-key
I put the passphase in the ~/.gnupg/gpg.conf file so I don't have to type it everytime.
  passphrase <pass_phrase>
Backup the ~/.gnupg/*.gpg files (pubring.gpg and secring.gpg)

Then run update.sh to create the repo files and then upload everything to website that is specifed in javaforce.list

To use the repo copy javaforce.list to /etc/apt/sources.list.d
and copy javaforce.gpg to /etc/apt/trusted.gpg.d
This is performed by the iso creation scripts.

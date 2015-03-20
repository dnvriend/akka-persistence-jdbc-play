#akka-persistence-jdbc-play
A small example how to use the [akka-persistence](http://doc.akka.io/docs/akka/snapshot/scala/persistence.html) with
the (Typesafe Play)[https://www.playframework.com/].

# Please have the following installed:

## (brew)[http://brew.sh/]

Type the following on a console:

    ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    brew update

## (brew cask)[http://caskroom.io/]

Type the following on a console:

    brew install caskroom/cask/brew-cask
    brew tap caskroom/versions
    brew cask install java
    brew cask install java6
    brew cask install java7
    brew cask install boot2docker
    brew cask install virtualbox

## Sofware!
And more software, type the following:
 
    brew install scala
    brew install sbt
    brew install typesafe-activator
    brew install boot2docker
    brew install docker-compose

## Optionally
If you wish, some other tools I like:

    brew install httpie
    brew install mc

## Upgrading packages
The nice thing about any package manager, and `brew` is just another package manager, is that you don't have to manage
the versioning of the packages yourself. To update/upgrade, just type `brew update` and `brew upgrade` to have the
package manager check the versions, the dependencies and do it all for you. How nice is that!

    brew update
    brew upgrade

## Install the VirtualBox extensions pack
First install the (Virtualbox extension pack)[https://www.virtualbox.org/wiki/Downloads] from the downloads page, and
just click on the download when finished. This will launch VirtualBox which will ask for your password to install it.

## Initialize boot2docker
Side note, why not docker-machine? Let it ripe a bit more :-)

Initialize boot2docker. I like to tweak it a bit. For development I like more disk space, and more RAM:

    boot2docker init --disksize=80000 --memory=8096

Make a small change to your ~/.bash_profile, add the following line:

    $(boot2docker shellinit)

Reload the change to your active console:

    source ~/.bash_profile

Make a small change to your /etc/hosts, add the following line, so you can use nice urls like: http://boot2docker:9000
    
    192.168.59.103 boot2docker

Launch boot2docker
    
    boot2docker up
    
# How to use the example
The example uses boot2docker, docker, docker-compose and typesafe-activator. 

To run the example, execute the run.sh script:

    ./run.sh
    
To restart the solution, because it uses akka-persistence, type:

    docker-compose restart
    
To remove all containers type:

    docker-compose rm --force
    
To view the log output type:

    docker-compose logs
    
When the example has been started, point your browser to:

    http://boot2docker:9000
    
Refresh the page a couple of times and see the counter state change.

Have fun!


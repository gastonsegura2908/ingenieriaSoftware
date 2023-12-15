# GIT CLI COMMANDS

Useful git commands for working in the terminal

- Ayuda
```sh
git help
git help <command>
```

- Create Branch:
```sh
git branch <new_branch_name>
```

- Switch Branch
```sh
git checkout <branch_name>
```

Esto no crea la rama en el remote (Gitlab o Github), para eso hay que hacer un push como el siguiente:
```sh
git push --set-upstream origin <branch_name>
```

- Check Current Status
```sh
git status
```

Output example:
> On branch main -> rama actual

   Your branch is up to date with 'origin/main'. -> actualizada al remote

   nothing to commit, working tree clean -> sin cambios locales para subir

- Update current branch from remote
```sh
git pull
```

- Para realizar un commit:
Primero agregamos los cambios que realizamos:
```sh
git add <archivo con cambios que queremos commitear o . para agregar todos los cambios>
```

Si hacemos git status obtenemos:
>On branch 9-corregir-tema
   Changes to be committed:
   (use "git restore --staged < file >..." to unstage)
  <span style="color:green">modified:  README.md</span>
 
- Creamos el commit siempre con un mensaje muy descriptivo: 
```sh
git commit -m "<Mensaje>"
```

Una vez realizado el commit local se puede proceder a realizar el push al remote

- Para enviar los commits locales al remote, envia los cambios de la rama actual a la rama del mismo nombre en el remote  
```sh
git push
```

- Merge, fusionar dos ramas: fusiona la rama actual con la rama especificada, NO CREA UNA REQUEST, sino que directamente las fusiona
```sh
git merge <branch_name>
```
Los merge request no se pueden crear desde la terminal sino sobre la plataforma remota (Gitlab/Github)
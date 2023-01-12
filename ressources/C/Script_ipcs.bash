#! /bin/bash

#  
# Liste les IPCs d'un login particulier
# et génère la commande pour la suppression
# (ou l'exécute directement # à décommenter )
#

if [[ $# == 0 ]]
then 
   proprio="colotte";
   echo "propriétaire par défaut : $proprio";
   echo "(Rappel de l'usage : Script  [login du proprio])";
else
   proprio=$1;
   echo "propriétaire : $proprio";
fi

echo "[à copier-coller :]"
ipcs -m | awk '{if($3==proprio){printf("ipcrm -m %d \n", $2);}}' proprio=$proprio ;
ipcs -s | awk '{if($3==proprio){printf("ipcrm -s %d \n", $2);}}' proprio=$proprio ;
ipcs -q | awk '{if($3==proprio){printf("ipcrm -q %d \n", $2);}}' proprio=$proprio ;

#ou le faire
#`ipcs -m | awk '{if($3==proprio){printf("ipcrm -m %d \n", $2);}}' proprio=$proprio `;
#`ipcs -s | awk '{if($3==proprio){printf("ipcrm -s %d \n", $2);}}' proprio=$proprio `;
#`ipcs -q | awk '{if($3==proprio){printf("ipcrm -q %d \n", $2);}}' proprio=$proprio `;

exit 0 ;

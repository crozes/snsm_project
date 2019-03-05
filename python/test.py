# -*- coding: utf-8 -*
# !/usr/bin/python3
# On importe Tkinter
from tkinter import *
from tkinter import messagebox
from PIL import ImageTk, Image
import json
import os,sys
from time import *

fenetre = Tk()
cpt=0
for i in range(0,10):
    
    globals()['SceneFrame%s'%i]=Frame(fenetre, width=250, height=50, borderwidth=1, background='yellow')
    globals()['SceneFrame%s'%i].pack(fill="x")
   
print(SceneFrame9)


files=os.listdir(path)
    
for file in files:
    it=ItemList(path+file)
    items.append(it)
    
for i in items:
    liste.insert(END, "Nom: "+i.nom+" | Durée: "+strftime('%M : %S',gmtime((i.duree)/1000))+ "\nSens: "+i.navigation+"\nEtat: "+i.etat+"\nDate: "+i.date)

liste.pack( side ="left", fill ="both" )
scrollbar.config( command = liste.yview )
fenetre.mainloop()
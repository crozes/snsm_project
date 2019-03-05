# -*- coding: utf-8 -*
# !/usr/bin/python3
from tkinter import messagebox
from PIL import ImageTk, Image
import json
import os,sys
from time import *
from tkinter import *


def __init__(self): 

    # Fonction permettant de lier le déplacement de la fenêtre avec la molette de la souris 
    def scrollEvent(event): 
        print (event.delta )
        if event.delta >0: 
            print ('déplacement vers le haut' )
            self.liste.yview_scroll(-2,'units') 

        else: 
            print ('déplacement vers le bas' )
            self.liste.yview_scroll(2,'units') 

    # Lorsque l'on rentre dans la fenêtre, on active la molette 
    def enrEnter(event): 
        self.root.bind('<MouseWheel>', scrollEvent) 

    # Lorsque l'on sort de la fenêtre, on désactive la liaison avec la molette 
    def enrLeave(event): 
        self.root.unbind('<MouseWheel>') 

# Création de la fenêtre principale 
self = Tk() 
# Création de la scrollbar 
self.scroll=Scrollbar(self.root,orient=VERTICAL) 
self.scroll.grid(row=0,column=1,sticky=N+S) 
self.scroll1=Scrollbar(self.root,orient=HORIZONTAL) 
self.scroll1.grid(row=1,column=0,sticky=E+W) 
# Création du canevas qui contient la frame qui contient les boutons 
self.liste =Canvas(self.root,yscrollcommand=self.scroll.set,xscrollcommand=self.scroll1.set) 

self.root.grid_columnconfigure(0,weight=1) 
self.root.grid_rowconfigure(0,weight=1) 

# Création de la frame, dans le canevas, qui contient les boutons 
self.listeBout=Frame(self.liste) 



# Création des boutons 
for i in range(20): 
    titre=str(i) 
    bou=Checkbutton(self.listeBout,text=titre) 
    bou.grid(sticky=N) 

# Pack du canevas 
self.liste.grid(row=0,column=0) 
# Configuration de la scrollbar 
self.scroll.config(command=self.liste.yview) 
self.scroll1.config(command=self.liste.xview) 
# Positionnement du canevas au début 
self.liste.create_window(0,0,window=self.listeBout) 
self.listeBout.update_idletasks() 
self.liste.config(scrollregion=self.liste.bbox('all')) 
self.liste.yview_moveto(0) 
self.liste.xview_moveto(0) 


self.root.bind('<Enter>',enrEnter) 
self.root.bind('<Leave>',enrLeave) 

self.root.mainloop() 


barreDefil=Scroll()
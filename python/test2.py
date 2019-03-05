# -*- coding: utf-8 -*
# !/usr/bin/python3
from tkinter import messagebox
from PIL import ImageTk, Image
import json
import os,sys
from time import *
from tkinter import *

path = "/home/pi/scene/"
items = []
class ItemList:

    def __init__(self,path):
        try:
            json_data=open(path,'r')
            data_dict=json.load(json_data)
            self.nom=data_dict["nom"]
            self.duree=data_dict["duree"]
            self.navigation=data_dict["navigation"]
            self.etat=data_dict["etat"]
            self.date=data_dict["date"]
            self.data=data_dict["data"]
            json_data.close()
            
        except ValueError:
            messagebox.showinfo("Alerte", "échec d'ouverture du fichier"+path)


def load_object(path,FrameName):
    files=os.listdir(path)
    
    for file in files:
      it=ItemList(path+file)
      items.append(it)
    cpt=0
    for i in items:
        globals()['SceneFrame%s'%cpt]=Frame(FrameName, width=250, height=50, borderwidth=1, background='yellow')
        globals()['SceneFrame%s'%cpt].pack(fill="x")
        globals()['champ_label%s'%cpt] = Label(globals()['SceneFrame%s'%cpt], text="Nom: "+i.nom+" | Durée: "+strftime('%M : %S',gmtime((i.duree)/1000))+ "\nSens: "+i.navigation+"\nEtat: "+i.etat+"\nDate: "+i.date)
        globals()['champ_label%s'%cpt].pack(fill="x")
        cpt+=1
    
 
root = Tk()
vsb = Scrollbar(root, orient=VERTICAL)
vsb.grid(row=0, column=1, sticky=N+S)
hsb = Scrollbar(root, orient=HORIZONTAL)
hsb.grid(row=1, column=0, sticky=E+W)
c = Canvas(root,yscrollcommand=vsb.set, xscrollcommand=hsb.set)
c.grid(row=0, column=0, sticky="news")
vsb.config(command=c.yview)
hsb.config(command=c.xview)
root.grid_rowconfigure(0, weight=1)
root.grid_columnconfigure(0, weight=1)
fr = Frame(c)
#On ajoute des widgets :
load_object(path,fr)
c.create_window(1, 1,  window=fr)
fr.update_idletasks()
c.config(scrollregion=c.bbox("all"))
root.mainloop()
import RF from "@/api/RF";
import axios from "axios";
import { defineStore } from "pinia";

export const useCeoMyStore = defineStore("CeoMy", {
  state: () => {
    const myData = {
      category: "",
      description: "",
      name: "",
      phone: "",
    };

    const newMenuData = {
      name: null,
      price: null,
      description: null,
    };
    const newMenuDataList = [
      {
        name: null,
        price: null,
        description: null,
      },
    ];
    const createImgUrl = null;
    const createMenuImgList = []
    const createMenuImgUrlList = [];
    const myTypeData = {
      modalView: false,
      newMenuIndex: 0,
      myCategoryIndex: 0,
      truckImg: null,
      is_update: false,
    };
    return {
      myData,
      newMenuData,
      myTypeData,
      newMenuDataList,
      createImgUrl,
      createMenuImgList,
      createMenuImgUrlList,
    };
  },
  actions: {
    updateNewMenu() {
      //초기화
      URL.revokeObjectURL(this.createImgUrl);
      this.createImgUrlList.forEach(function (item) {
        URL.revokeObjectURL(item);
      });
      location.reload();
    },
    setNewMenu() {
      const token = localStorage.getItem("accessToken");
      const menuList = {
        menuReqList: [
        ]
      }
      menuList.menuReqList = this.newMenuDataList.slice(0, -1)
      console.log(menuList)
      axios({
        url: RF.menu.setMenu(),
        method: "post",
        headers: { Authorization: "Bearer " + token },
        data: menuList
      })
        .then((res) => {
          console.log(res)
          console.log(res.data)
        })
        .catch((err) => {
          console.log(err);
        });
    },
    setFoodTruck() {
      // let formData = new FormData()
      // formData.append('file', this.myTypeData.truckImg)
      const token = localStorage.getItem("accessToken");
      axios({
        url: RF.foodtruck.registerFoodTruck(),
        method: "post",
        headers: { Authorization: "Bearer " + token, },
        data: this.myData,
      })
        .then(() => {
          this.setImg()
        })
        .catch((err) => {
          console.log(err);
        });
    },
    updateFoodTruck() {
      const token = localStorage.getItem("accessToken");
      axios({
        url: RF.foodtruck.updateFoodTruck(),
        method: "patch",
        headers: { Authorization: "Bearer " + token },
        data: this.myData,
      })
        .then((res) => {
          console.log(res)
          if (this.myTypeData.truckImg !== null) {
            this.setImg()
          }

        })
        .catch((err) => {
          console.log(err);
        });
    },
    getMyFoodTruck() {
      const token = localStorage.getItem("accessToken");
      const truckId = sessionStorage.getItem("foodTruck")
      axios({
        url: RF.foodtruck.getFoodTruck(truckId),
        method: "get",
        headers: { Authorization: "Bearer " + token },
      })
        .then((res) => {
          this.myData.category = res.data.category
          this.myData.description = res.data.description
          this.myData.name = res.data.name
          this.myData.phone = res.data.phone
          this.myTypeData.is_update = true
          console.log(res.data)
        })
        .catch((err) => {
          console.log(err);
        });
    },

    setImg() {
      var formData = new FormData();
      formData.append("file", this.myTypeData.truckImg);
      const token = localStorage.getItem("accessToken");
      axios({
        url: RF.foodtruck.setImg(),
        method: "post",
        headers: { Authorization: "Bearer " + token },
        data: formData,
      })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getImg() {
      const truckId = sessionStorage.getItem("foodTruck")
      const token = localStorage.getItem("accessToken");
      axios({
        url: RF.foodtruck.getImg(truckId),
        responseType: 'blob',
        method: "get",
        headers: { Authorization: "Bearer " + token },
      })
        .then((res) => {
          console.log(res.data)
          if (res.data !== null) {
            this.drawTruckImg(res)
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    // 아래 함수 임의 사용금지
    drawTruckImg(res) {

      if (this.createImgUrl !== null) {
        URL.revokeObjectURL(this.createImgUrl);
      }

      let imgTag = document.getElementById('my-truck-img')
      const url = URL.createObjectURL(new Blob([res.data], { type: res.headers['content-type'] }));
      // this.myData.truckImg = file;
      this.createImgUrl = url
      imgTag.nextElementSibling.src = url;
      imgTag.nextElementSibling.classList.remove("imgVisible");
      imgTag.nextElementSibling.nextElementSibling.classList.add(
        "imgVisible"
      );
    },

  },
});
